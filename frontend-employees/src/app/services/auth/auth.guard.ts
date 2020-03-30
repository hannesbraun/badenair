import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthService} from './auth.service';
import {UserType} from '../dtos/Dtos';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {

    constructor(private authService: AuthService) {
    }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

        const user = this.authService.getUser();
        if (!user) {
            return false;
        }

        const expectedRole: UserType = next.data.expectedRole;
        if (expectedRole === undefined) {
            return true;
        }

        if (user.type === expectedRole) {
            return true;
        }
        return false;
    }
}
