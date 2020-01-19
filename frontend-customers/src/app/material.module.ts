import {NgModule} from '@angular/core';

import {
    MAT_DATE_LOCALE,
    MatAutocompleteModule,
    MatButtonModule,
    MatCardModule,
    MatDatepickerModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatRadioModule,
    MatSelectModule,
    MatSidenavModule,
    MatToolbarModule
} from '@angular/material';

@NgModule({
    imports: [
        MatAutocompleteModule,
        MatButtonModule,
        MatCardModule,
        MatDatepickerModule,
        MatDialogModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatMenuModule,
        MatNativeDateModule,
        MatRadioModule,
        MatSelectModule,
        MatSidenavModule,
        MatToolbarModule
    ],
    exports: [
        MatAutocompleteModule,
        MatButtonModule,
        MatCardModule,
        MatDatepickerModule,
        MatDialogModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatMenuModule,
        MatNativeDateModule,
        MatRadioModule,
        MatSelectModule,
        MatSidenavModule,
        MatToolbarModule
    ],
    providers: [
        {provide: MAT_DATE_LOCALE, useValue: 'de-DE'}
    ]
})
export class MaterialModule {
}
