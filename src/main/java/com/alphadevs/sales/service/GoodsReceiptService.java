package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.*;
import com.alphadevs.sales.repository.GoodsReceiptDetailsRepository;
import com.alphadevs.sales.repository.GoodsReceiptRepository;
import com.alphadevs.sales.security.SecurityUtils;
import com.alphadevs.sales.service.dto.PurchaseAccountBalanceCriteria;
import com.alphadevs.sales.service.dto.StockCriteria;
import com.alphadevs.sales.service.dto.SupplierAccountBalanceCriteria;
import com.alphadevs.sales.service.dto.TransactionTypeCriteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.alphadevs.sales.security.AuthoritiesConstants.ADMIN;

/**
 * Service Implementation for managing {@link GoodsReceipt}.
 */
@Service
@Transactional
public class GoodsReceiptService {

    private final Logger log = LoggerFactory.getLogger(GoodsReceiptService.class);

    private final GoodsReceiptRepository goodsReceiptRepository;
    private final GoodsReceiptDetailsRepository goodsReceiptDetailsRepository;
    private final UserService userService;
    private final StockService stockService;
    private final StockQueryService stockQueryService;
    private final PurchaseAccountBalanceQueryService purchaseAccountBalanceQueryService;
    private final ItemBinCardService itemBinCardService;
    private final ItemsService itemsService;
    private final PurchaseAccountService purchaseAccountService;
    private final SupplierAccountService supplierAccountService;
    private final SupplierAccountBalanceQueryService supplierAccountBalanceQueryService;
    private final CashBookService cashBookService;
    private final CashBookBalanceQueryService cashBookBalanceQueryService;
    private final TransactionTypeQueryService transactionTypeQueryService;

    public GoodsReceiptService(GoodsReceiptRepository goodsReceiptRepository, GoodsReceiptDetailsRepository goodsReceiptDetailsRepository, UserService userService, StockService stockService, StockQueryService stockQueryService, PurchaseAccountBalanceQueryService purchaseAccountBalanceQueryService, ItemBinCardService itemBinCardService, ItemsService itemsService, PurchaseAccountService purchaseAccountService, SupplierAccountService supplierAccountService, SupplierAccountBalanceQueryService supplierAccountBalanceQueryService, CashBookService cashBookService, CashBookBalanceQueryService cashBookBalanceQueryService, TransactionTypeQueryService transactionTypeQueryService) {
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.goodsReceiptDetailsRepository = goodsReceiptDetailsRepository;
        this.userService = userService;
        this.stockService = stockService;
        this.stockQueryService = stockQueryService;
        this.purchaseAccountBalanceQueryService = purchaseAccountBalanceQueryService;
        this.itemBinCardService = itemBinCardService;
        this.itemsService = itemsService;
        this.purchaseAccountService = purchaseAccountService;
        this.supplierAccountService = supplierAccountService;
        this.supplierAccountBalanceQueryService = supplierAccountBalanceQueryService;
        this.cashBookService = cashBookService;
        this.cashBookBalanceQueryService = cashBookBalanceQueryService;
        this.transactionTypeQueryService = transactionTypeQueryService;
    }

    /**
     * Save a goodsReceipt.
     *
     * @param goodsReceipt the entity to save.
     * @return the persisted entity.
     */
    public GoodsReceipt save(GoodsReceipt goodsReceipt) {
        log.debug("Request to save GoodsReceipt : {}", goodsReceipt);
        GoodsReceipt savedGoodsReceipt = new GoodsReceipt();
        ExUser exUser = null;

        //Filter Variables
        LongFilter longFilterCompanyId = new LongFilter();
        LongFilter longFilterLocationId = new LongFilter();
        LongFilter longFilterItemId = new LongFilter();
        LongFilter longFilterSupplierId = new LongFilter();

        //FixMe - HardCoding the Transaction Type
        LongFilter longFilterTransactionId = new LongFilter();
        longFilterTransactionId.setEquals(1L);
        BooleanFilter booleanFilterTrue = new BooleanFilter();
        booleanFilterTrue.setEquals(true);

        //get Transactional Type
        TransactionTypeCriteria transactionTypeCriteria = new TransactionTypeCriteria();
        transactionTypeCriteria.setIsActive(booleanFilterTrue);
        transactionTypeCriteria.setId(longFilterTransactionId);
        List<TransactionType> transactionType = transactionTypeQueryService.findByCriteria(transactionTypeCriteria);

        //Check if TransactionType is Empty or have multiple entries
        assert (transactionType.isEmpty());
        assert (transactionType.size()>1);


        if(userService.getExUser().isPresent()){
            exUser = userService.getExUser().get();
            assert (exUser != null);
        }
        //Check if the user had Location access - for extra security
        if(exUser != null && exUser.getLocations().contains(goodsReceipt.getLocation())){
            savedGoodsReceipt = goodsReceiptRepository.save(goodsReceipt);
            BigDecimal totalAmount = new BigDecimal(0);
            for (GoodsReceiptDetails details : goodsReceipt.getDetails()) {
                if(details != null){
                    GoodsReceiptDetails savedGoodsReceiptDetails = null;
                    details.setGrn(savedGoodsReceipt);
                    savedGoodsReceiptDetails = goodsReceiptDetailsRepository.save(details);


                    //Set Amounts
                    totalAmount = totalAmount.add(savedGoodsReceiptDetails.getRevisedItemCost());

                    //Setting Filter
                    longFilterCompanyId.setEquals(exUser.getCompany().getId());
                    longFilterLocationId.setEquals(savedGoodsReceipt.getLocation().getId());
                    longFilterItemId.setEquals(savedGoodsReceiptDetails.getItem().getId());
                    longFilterSupplierId .setEquals(savedGoodsReceipt.getSupplier().getId());

                    //getCurrent Stock
                    StockCriteria stockCriteria = new StockCriteria();
                    stockCriteria.setCompanyId(longFilterCompanyId);
                    stockCriteria.setLocationId(longFilterLocationId);
                    stockCriteria.setItemId(longFilterItemId);
                    List<Stock> StockItem = stockQueryService.findByCriteria(stockCriteria);

                    //Check if StockItem is Empty or have multiple entries
                    assert (StockItem.isEmpty());
                    assert (StockItem.size()>1);

                    //Update Stock
                    Stock stock = new Stock();
                    stock.company(exUser.getCompany());
                    stock.setItem(savedGoodsReceiptDetails.getItem());
                    stock.setLocation(savedGoodsReceipt.getLocation());
                    stock.setStockQty(StockItem.get(0).getStockQty() + savedGoodsReceiptDetails.getGrnQty());
                    stockService.save(stock);

                    //Update ItemBin Card
                    ItemBinCard itemBinCard = new ItemBinCard();
                    itemBinCard.setItem(savedGoodsReceiptDetails.getItem());
                    itemBinCard.setLocation(savedGoodsReceipt.getLocation());
                    //If user level is Admin, allow user to override the date if not set current date/time
                    itemBinCard.setTransactionDate(SecurityUtils.isCurrentUserInRole(ADMIN) ? savedGoodsReceipt.getGrnDate() : java.time.LocalDate.now());
                    itemBinCard.setTransactionQty(savedGoodsReceiptDetails.getGrnQty());
                    itemBinCard.setTransactionDescription("Goods Receipt : " + savedGoodsReceipt.getGrnNumber() );
                    itemBinCard.setTransactionBalance(new BigDecimal(stock.getStockQty()));
                    itemBinCardService.save(itemBinCard);

                }
            }

            //getCurrent Purchase Balance
            PurchaseAccountBalanceCriteria purchaseAccountBalanceCriteria = new PurchaseAccountBalanceCriteria();
            purchaseAccountBalanceCriteria.setLocationId(longFilterLocationId);
            List<PurchaseAccountBalance> purchaseAccountBalanceByCriteria = purchaseAccountBalanceQueryService.findByCriteria(purchaseAccountBalanceCriteria);

            //Check if PurchaseAccountBalance is Empty or have multiple entries
            assert (purchaseAccountBalanceByCriteria.isEmpty());
            assert (purchaseAccountBalanceByCriteria.size()>1);

            //Update Purchase Account
            PurchaseAccount purchaseAccount = new PurchaseAccount();
            purchaseAccount.setLocation(savedGoodsReceipt.getLocation());
            purchaseAccount.setTransactionDate(SecurityUtils.isCurrentUserInRole(ADMIN) ? savedGoodsReceipt.getGrnDate() : java.time.LocalDate.now());
            purchaseAccount.setTransactionAmountCR(new BigDecimal(0));
            purchaseAccount.setTransactionAmountDR(totalAmount);
            purchaseAccount.setTransactionDescription("Purchase : " + savedGoodsReceipt.getGrnNumber());
            purchaseAccount.setTransactionBalance(purchaseAccountBalanceByCriteria.get(0).getBalance().add(totalAmount));
            purchaseAccount.setTransactionType(transactionType.get(0));
            purchaseAccountService.save(purchaseAccount);

            //get Supplier Balance
            SupplierAccountBalanceCriteria supplierAccountBalanceCriteria = new SupplierAccountBalanceCriteria();
            supplierAccountBalanceCriteria.setLocationId(longFilterLocationId);
            supplierAccountBalanceCriteria.setTransactionTypeId(longFilterTransactionId);
            supplierAccountBalanceCriteria.setSupplierId(longFilterSupplierId);
            List<SupplierAccountBalance> supplierAccountBalance = supplierAccountBalanceQueryService.findByCriteria(supplierAccountBalanceCriteria);

            //Check if SupplierAccountBalance is Empty or have multiple entries
            assert (supplierAccountBalance.isEmpty());
            assert (supplierAccountBalance.size()>1);

            SupplierAccount supplierAccount = new SupplierAccount();
            supplierAccount.setLocation(savedGoodsReceipt.getLocation());
            supplierAccount.setTransactionDate(SecurityUtils.isCurrentUserInRole(ADMIN) ? savedGoodsReceipt.getGrnDate() : java.time.LocalDate.now());
            supplierAccount.setTransactionAmountCR(totalAmount);
            supplierAccount.setTransactionAmountDR(new BigDecimal(0));
            supplierAccount.setTransactionDescription("Purchase : " + savedGoodsReceipt.getGrnNumber());
            supplierAccount.setTransactionBalance(supplierAccountBalance.get(0).getBalance().add(totalAmount));
            supplierAccount.setTransactionType(transactionType.get(0));
            supplierAccountService.save(supplierAccount);

        }


        return savedGoodsReceipt;
    }

    /**
     * Get all the goodsReceipts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GoodsReceipt> findAll(Pageable pageable) {
        log.debug("Request to get all GoodsReceipts");
        return goodsReceiptRepository.findAll(pageable);
    }


    /**
     * Get one goodsReceipt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GoodsReceipt> findOne(Long id) {
        log.debug("Request to get GoodsReceipt : {}", id);
        return goodsReceiptRepository.findById(id);
    }

    /**
     * Delete the goodsReceipt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GoodsReceipt : {}", id);
        goodsReceiptRepository.deleteById(id);
    }
}
