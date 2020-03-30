import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';
import {AuthService} from '../../services/auth/auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent {

    loginForm: FormGroup;
    isValidUser = true;

    constructor(private dialog: MatDialogRef<LoginComponent>,
                private fb: FormBuilder,
                private authService: AuthService) {

        this.loginForm = this.fb.group({
            username: ['', Validators.required],
            password: ['', Validators.required]
        });
    }

    login() {
        const {username, password} = this.loginForm.value;
        const user = this.authService.login(username, password);

        if (user) {
            this.isValidUser = true;
            this.dialog.close(user);
        } else {
            this.isValidUser = false;
        }
    }
}
