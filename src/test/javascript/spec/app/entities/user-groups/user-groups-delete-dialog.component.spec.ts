import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WikunumTestModule } from '../../../test.module';
import { UserGroupsDeleteDialogComponent } from 'app/entities/user-groups/user-groups-delete-dialog.component';
import { UserGroupsService } from 'app/entities/user-groups/user-groups.service';

describe('Component Tests', () => {
  describe('UserGroups Management Delete Component', () => {
    let comp: UserGroupsDeleteDialogComponent;
    let fixture: ComponentFixture<UserGroupsDeleteDialogComponent>;
    let service: UserGroupsService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [UserGroupsDeleteDialogComponent]
      })
        .overrideTemplate(UserGroupsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserGroupsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserGroupsService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
