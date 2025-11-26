import * as fns from 'date-fns'

export const formatDistanceToNow = (date: string) => {
    return fns.formatDistanceToNow(new Date(date), { addSuffix: true })
}
