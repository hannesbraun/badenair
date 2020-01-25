import {FormGroup} from '@angular/forms';

export function getFormControlValueFunction(formGroup: FormGroup): (formControlName: string) => any | null {
    return formControlName => {
        const formControl = formGroup.get(formControlName);
        return formControl ? formControl.value : null;
    };
}
