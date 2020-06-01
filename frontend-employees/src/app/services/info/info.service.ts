import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';

@Injectable({
    providedIn: 'root'
})
export class InfoService {

    constructor(private snackBar: MatSnackBar) {
    }

    showMessage(message: string) {
        this.snackBar.open(message, 'Schließen');
    }

    showErrorMessage(message: string) {
        this.snackBar.open(message, 'Schließen', {
            panelClass: ['error-message'], verticalPosition: 'top'});
    }
}
