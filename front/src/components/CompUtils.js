export function afterNow(date) {
    return new Date(date).getTime() > Date.now()
}