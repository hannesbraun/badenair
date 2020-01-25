import {Component} from '@angular/core';
import {SignUpDto} from '../../services/dtos/Dtos';
import {SignUpService} from '../../services/signup/sign-up.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-sign-up-page',
    templateUrl: './sign-up-page.component.html',
    styles: ['']
})
export class SignUpPageComponent {

    constructor(private signUpService: SignUpService, private router: Router) {
    }

    onSignUpSubmit(signUpDto: SignUpDto) {
        this.signUpService.signUpNewUser(signUpDto).subscribe(dto => {
            this.router.navigate(['']);
        });
    }
}
