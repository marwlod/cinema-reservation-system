export const baseUrl = "http://localhost:8888"
export const loginSubUrl = "/login"
export const registerSubUrl = "/register"
export const profileSubUrl = "/showProfile"
export const moviesSubUrl = "/showMovies"
export const availableSeatsSubUrl = "/showFreeSeats"
export const reserveSeatSubUrl = "/reserveSeat"
export const showSeatReservationsSubUrl = "/showReservations/seat"
export const showHallReservationsSubUrl = "/showReservations/hall"
export const payForSeatSubUrl = "/payForSeat"
export const payAdvanceForHallSubUrl = "/payAdvanceForHall"
export const payForHallSubUrl = "/payForHall"
export const verifyAdminSubUrl = "/verifyAdmin"
export const availableCinemaHallsUrl = "/showAvailableCinemaHalls"
export const reserveHallSubUrl = "/reserveHall"
export const statisticsGeneralSubUrl = "/showStatistics"
export const statisticsMovieSubUrl = "/showStatistics/movie"
export const statisticsHallSubUrl = "/showStatistics/hall"

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

export function apiDateNow() {
    return apiDateNowPlusDays(0)
}

export function apiDateNowMinusDays(toSubtract) {
    return apiDateNowPlusDays(-toSubtract)
}

export function apiDateNowPlusDays(toAdd) {
    const date = new Date(Date.now() + toAdd*24*60*60*1000)
    const days = date.getDate()
    const month = date.getMonth() + 1 // months numbered from 0 to 11
    const year = date.getFullYear()
    return year + "-" +
        (month < 10 ? "0" + month : month) + "-" +
        (days < 10 ? "0" + days : days)
}

function correctDateAndTimeTimeZone(dateAndTime) {
    const dateTime = new Date(dateAndTime);
    dateTime.setMonth(dateTime.getMonth() + 1);
    return dateTime;
}

export function formatDateAndTime(dateAndTime) {
    const dateTimeZoneCorrected = correctDateAndTimeTimeZone(dateAndTime);
    const year = dateTimeZoneCorrected.getFullYear();
    const month = dateTimeZoneCorrected.getMonth();
    const day = dateTimeZoneCorrected.getDate();
    const hours = dateTimeZoneCorrected.getHours();
    const minutes = dateTimeZoneCorrected.getMinutes();
    const seconds = dateTimeZoneCorrected.getSeconds();
    return year + "-" +
        (month < 10 ? "0" + month : month) + "-" +
        (day < 10 ? "0" + day : day) + " " +
        (hours < 10 ? "0" + hours : hours) + ":" +
        (minutes < 10 ? "0" + minutes : minutes) + ":" +
        (seconds < 10 ? "0" + seconds : seconds);
}

export function formatDate(dateAndTime) {
    const dateTimeZoneCorrected = correctDateAndTimeTimeZone(dateAndTime);
    const year = dateTimeZoneCorrected.getFullYear();
    const month = dateTimeZoneCorrected.getMonth();
    const day = dateTimeZoneCorrected.getDate();
    return year + "-" +
        (month < 10 ? "0" + month : month) + "-" +
        (day < 10 ? "0" + day : day);
}

export function dayAndMonthNowMinusDays(toSubtract) {
    const date = new Date(Date.now() - toSubtract*24*60*60*1000)
    const days = date.getDate()
    const month = date.getMonth() + 1 // months numbered from 0 to 11
    return (days < 10 ? "0" + days : days) + "-" + (month < 10 ? "0" + month : month)
}

export function formatMoney(money) {
    const moneyJSON = JSON.stringify(money);
    const moneyToNumber = Number(moneyJSON);
    return moneyToNumber.toFixed(2);
}