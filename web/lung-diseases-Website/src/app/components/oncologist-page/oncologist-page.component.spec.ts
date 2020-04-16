import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OncologistPageComponent } from './oncologist-page.component';

describe('OncologistPageComponent', () => {
  let component: OncologistPageComponent;
  let fixture: ComponentFixture<OncologistPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OncologistPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OncologistPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
