const iso8601 = /^\d{4}-\d\d-\d\dT\d\d:\d\d:\d\d(\.\d+)?(([+-]\d\d:\d\d)|Z)?$/;

export function convertToDate(body: any) {
    if (body === null || body === undefined) {
        return body;
    }

    if (typeof body !== 'object') {
        return body;
    }

    for (const key of Object.keys(body)) {
        const value = body[key];
        if (isIso8601(value)) {
            body[key] = new Date(value);
        } else if (typeof value === 'object') {
            convertToDate(value);
        }
    }
}

export function isIso8601(value: any) {
    if (value === null || value === undefined) {
        return false;
    }

    return iso8601.test(value);
}
