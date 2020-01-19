import {Component, OnInit} from '@angular/core';
import {SignUpDto} from '../../services/dtos/Dtos';

@Component({
    selector: 'app-sign-up-page',
    templateUrl: './sign-up-page.component.html',
    styles: ['']
})
export class SignUpPageComponent implements OnInit {

    constructor() {
    }

    ngOnInit() {
    }

    onSignUpSubmit(signUpDto: SignUpDto) {
        // TODO: call service to handle sign up
        console.log(signUpDto);
    }
}
