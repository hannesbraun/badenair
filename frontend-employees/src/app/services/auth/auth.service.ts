import {Injectable} from '@angular/core';
import {UserType} from './auth.guard';

interface User {
    type: UserType;
    name: string;
}


@Injectable({
    providedIn: 'root'
})
export class AuthService {

    user!: User;

    constructor() {
    }

    login(username: string, password: string): boolean {
        // TODO: send request to Server
        switch (username) {
            case 'pilot':
                this.user = {
                    name: username,
                    type: UserType.pilot
                };
                return true;

            case 'technician':
                this.user = {
                    name: username,
                    type: UserType.technician
                };
                return true;

            case 'ground':
                this.user = {
                    name: username,
                    type: UserType.ground
                };
                return true;

            case 'flightDirector':
                this.user = {
                    name: username,
                    type: UserType.flightDirector
                };
                return true;

            default:
                return false;
        }
    }

    getUser(): User | undefined {
        return this.user;
    }
}
