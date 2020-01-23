import {Injectable} from '@angular/core';
import {SignUpDto} from '../dtos/Dtos';
import {Observable, of} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SignUpService {

    constructor() {
    }

    signUpNewUser(signUpDto: SignUpDto): Observable<SignUpDto> {
        return of(signUpDto);
    }
}
