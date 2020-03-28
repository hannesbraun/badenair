import {Injectable} from '@angular/core';
import {User, UserType} from '../dtos/Dtos';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    user!: User;

    constructor() {
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
        return newUser;
    }

    getUser(): User | undefined {
        return this.user;
    }
}
