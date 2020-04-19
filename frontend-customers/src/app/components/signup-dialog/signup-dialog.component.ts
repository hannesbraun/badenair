import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
    selector: 'app-signup-dialog',
    templateUrl: './signup-dialog.component.html',
    styleUrls: ['./signup-dialog.component.scss']
})
export class SignupDialogComponent implements OnInit {

    profileForm: FormGroup = this.formBuilder.group({
        profile: []
    });

    constructor(private formBuilder: FormBuilder, private dialog: MatDialogRef<SignupDialogComponent>) {
    }

    onSubmit() {
        if (this.profileForm.valid) {
            this.dialog.close();
        }
    }

    ngOnInit() {
    }

}
