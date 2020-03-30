import {Injectable} from '@angular/core';
import {User, UserType} from '../dtos/Dtos';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    user: User | undefined;

    constructor() {
        // TODO: replace with access Token
        const username = localStorage.getItem('username');
        if (username) {
            this.login(username, '');
        }
    }

    login(username: string, password: string): User | undefined {
        const newUser = {name: username} as User;

        // TODO: send request to Server
        switch (username) {
            case 'pilot':
                newUser.type = UserType.pilot;
                break;

            case 'technician':
                newUser.type = UserType.technician;
                break;

            case 'ground':
                newUser.type = UserType.ground;
                break;

            case 'flightDirector':
                newUser.type = UserType.flightDirector;
                break;

            default:
                return undefined;
        }
        this.user = newUser;
        // TODO: replace with access Token
        localStorage.setItem('username', newUser.name);
        return newUser;
    }

    logout() {
        this.user = undefined;
        // TODO: replace with access Token
        localStorage.removeItem('username');
    }

    getUser(): User | undefined {
        return this.user;
    }
}
