import * as fns from 'date-fns'

export const dateFormatDistanceToNow = (date: string) => {
    return fns.formatDistanceToNow(new Date(date), { addSuffix: true })
}

export const dateFormat = (date: string, format?: string) => {
    return fns.format(new Date(date), format ?? 'MMM d yyyy')
}
