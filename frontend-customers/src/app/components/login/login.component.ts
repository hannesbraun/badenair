import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent {

    LoginForm: FormGroup;

    constructor(private dialog: MatDialogRef<LoginComponent>, private fb: FormBuilder, private router: Router) {
        this.LoginForm = this.fb.group({
            username: ['', Validators.required],
            password: ['', Validators.required]
        });
    }

    signUp() {
        this.router.navigate(['/signup']);
        this.dialog.close();
    }

    login() {
        this.dialog.close(true);
    }
}
