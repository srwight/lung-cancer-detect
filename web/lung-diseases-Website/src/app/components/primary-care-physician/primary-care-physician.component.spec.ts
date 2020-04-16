import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrimaryCarePhysicianComponent } from './primary-care-physician.component';

describe('PrimaryCarePhysicianComponent', () => {
  let component: PrimaryCarePhysicianComponent;
  let fixture: ComponentFixture<PrimaryCarePhysicianComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrimaryCarePhysicianComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrimaryCarePhysicianComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
