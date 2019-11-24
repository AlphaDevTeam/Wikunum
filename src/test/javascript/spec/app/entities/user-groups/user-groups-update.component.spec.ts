import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { UserGroupsUpdateComponent } from 'app/entities/user-groups/user-groups-update.component';
import { UserGroupsService } from 'app/entities/user-groups/user-groups.service';
import { UserGroups } from 'app/shared/model/user-groups.model';

describe('Component Tests', () => {
  describe('UserGroups Management Update Component', () => {
    let comp: UserGroupsUpdateComponent;
    let fixture: ComponentFixture<UserGroupsUpdateComponent>;
    let service: UserGroupsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [UserGroupsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UserGroupsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserGroupsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserGroupsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserGroups(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserGroups();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
