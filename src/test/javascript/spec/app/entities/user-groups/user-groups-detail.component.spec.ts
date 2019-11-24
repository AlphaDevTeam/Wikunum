import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { UserGroupsDetailComponent } from 'app/entities/user-groups/user-groups-detail.component';
import { UserGroups } from 'app/shared/model/user-groups.model';

describe('Component Tests', () => {
  describe('UserGroups Management Detail Component', () => {
    let comp: UserGroupsDetailComponent;
    let fixture: ComponentFixture<UserGroupsDetailComponent>;
    const route = ({ data: of({ userGroups: new UserGroups(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [UserGroupsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(UserGroupsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserGroupsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userGroups).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
