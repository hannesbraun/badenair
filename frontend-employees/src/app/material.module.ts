import {NgModule} from '@angular/core';

import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatDialogModule} from '@angular/material/dialog';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatMenuModule} from '@angular/material/menu';
import {MatSelectModule} from '@angular/material/select';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MAT_DATE_LOCALE} from '@angular/material/core';
import {MatInputModule} from '@angular/material/input';


@NgModule({
    imports: [
        MatButtonModule,
        MatMenuModule,
        MatToolbarModule,
        MatIconModule,
        MatCardModule,
        MatListModule,
        MatDialogModule,
        MatSelectModule,
        MatSidenavModule,
        MatInputModule,
    ],
    exports: [
        MatButtonModule,
        MatMenuModule,
        MatToolbarModule,
        MatIconModule,
        MatCardModule,
        MatListModule,
        MatDialogModule,
        MatSelectModule,
        MatSidenavModule,
        MatInputModule
    ],
    providers: [
        {provide: MAT_DATE_LOCALE, useValue: 'de-DE'}
    ]
})
export class MaterialModule {
}
