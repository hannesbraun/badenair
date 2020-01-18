import {NgModule} from '@angular/core';

import {
    MatButtonModule,
    MatCardModule,
    MatDialogModule,
    MatIconModule,
    MatListModule,
    MatMenuModule,
    MatSelectModule,
    MatToolbarModule
} from '@angular/material';
import {MatSidenavModule} from "@angular/material/sidenav";

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
        MatSidenavModule
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
    ]
})
export class MaterialModule {
}
