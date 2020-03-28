import {Injectable} from '@angular/core';

interface User {
    type: string;
    name: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    user!: User;

    constructor() {
    }

    login(userName: string, password: string): boolean {
        // TODO: send request to Server

        if (userName === 'pilot' && password === 'pilot') {
            return true;
        }
        return false;
    }

    getUser(): User | undefined {
        return undefined;
        return {
            type: 'pilot',
            name: 'Peter',
        };
    }
}
