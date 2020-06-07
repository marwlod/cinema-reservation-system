export const baseUrl = "http://localhost:8888"
export const loginSubUrl = "/login"
export const registerSubUrl = "/register"
export const profileSubUrl = "/showProfile"
export const moviesSubUrl = "/showMovies"
export const freeSeatsSubUrl = "/showFreeSeats"
export const reserveSeatSubUrl = "/reserveSeat"
export const showSeatReservationsSubUrl = "/showReservations/seat"
export const payForSeatSubUrl = "/payForSeat"
export const verifyAdminSubUrl = "/verifyAdmin"
export const statisticsGeneralSubUrl = "/showStatistics"
export const statisticsMovieSubUrl = "/showStatistics/movie/"
export const statisticsMovieName = "American Pie 5: Naked Mile"

export function buildUrl(subUrl, pathVars, urlParams) {
    const url = [baseUrl, subUrl];
    pathVars.forEach(pathVar => url.push("/", pathVar))
    let first = true;
    Object.keys(urlParams).forEach(paramKey => {
        if (first) {
            url.push("?")
            first = false;
        } else {
            url.push("&")
        }
        url.push(paramKey, "=", urlParams[paramKey])
    })
    return url.join("")
}

export function callCrsApi(url, params, onSuccess, onFail) {
    fetch(url, params)
        .then(res => res.json().catch(() => {
            if (res.status === 200) {
                return {success: ""}
            } else {
                return {status: "", message: ""}
            }
        }))
        .then((data) => {
            if (data.hasOwnProperty("status") && data.hasOwnProperty("message")) {
                onFail(data)
            } else {
                onSuccess(data)
            }
        })
        .catch(console.warn)
}

export function toApiDate(fullDate) {
    const date = fullDate.getDate()
    const month = fullDate.getMonth() + 1 // months numbered from 0 to 11
    const year = fullDate.getFullYear()
    return year + "-" +
        (month < 10 ? "0" + month : month) + "-" +
        (date < 10 ? "0" + date : date)
}