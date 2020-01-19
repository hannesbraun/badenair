import {FormGroup, ValidationErrors} from '@angular/forms';

export const passwordsMatchValidator = (control: FormGroup): ValidationErrors | null => {
    const password = control.get('password');
    const passwordConfirm = control.get('passwordConfirm');

    return password && passwordConfirm && password.value !== passwordConfirm.value ? {password: true} : null;
};
