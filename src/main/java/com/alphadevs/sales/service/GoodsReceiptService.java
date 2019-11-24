package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.*;
import com.alphadevs.sales.repository.GoodsReceiptDetailsRepository;
import com.alphadevs.sales.repository.GoodsReceiptRepository;
import com.alphadevs.sales.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final ItemBinCardService itemBinCardService;
    private final ItemsService itemsService;
    private final PurchaseAccountService purchaseAccountService;

    public GoodsReceiptService(GoodsReceiptRepository goodsReceiptRepository, GoodsReceiptDetailsRepository goodsReceiptDetailsRepository, UserService userService, StockService stockService, ItemBinCardService itemBinCardService, ItemsService itemsService, PurchaseAccountService purchaseAccountService) {
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.goodsReceiptDetailsRepository = goodsReceiptDetailsRepository;
        this.userService = userService;
        this.stockService = stockService;
        this.itemBinCardService = itemBinCardService;
        this.itemsService = itemsService;
        this.purchaseAccountService = purchaseAccountService;
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
        if(userService.getExUser().isPresent()){
            exUser = userService.getExUser().get();
        }
        //Check if the user had Location access - for extra security
        if(exUser != null && exUser.getLocations().contains(goodsReceipt.getLocation())){
            savedGoodsReceipt = goodsReceiptRepository.save(goodsReceipt);
            for (GoodsReceiptDetails details : goodsReceipt.getDetails()) {
                if(details != null){
                    details.setGrn(savedGoodsReceipt);
                    goodsReceiptDetailsRepository.save(details);

                    //Update Stock
                    Stock stock = new Stock();
                    stock.company(exUser.getCompany());
                    stock.setItem(details.getItem());
                    stock.setLocation(savedGoodsReceipt.getLocation());
                    stock.setStockQty(Double.valueOf(details.getGrnQty()));

                    //Update ItemBin Card
                    ItemBinCard itemBinCard = new ItemBinCard();
                    itemBinCard.setItem(details.getItem());
                    itemBinCard.setLocation(savedGoodsReceipt.getLocation());
                    //If Admin allow user to override the date if not set current date/time
                    itemBinCard.setTransactionDate(SecurityUtils.isCurrentUserInRole(ADMIN) ? savedGoodsReceipt.getGrnDate() : java.time.LocalDate.now());
                    itemBinCard.setTransactionQty(0.0);
                    itemBinCard.setTransactionDescription("Goods Receipt : " + savedGoodsReceipt.getGrnNumber() + ",");
                }
            }
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
