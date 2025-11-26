import { createColumnHelper } from '@tanstack/react-table'
import { Badge } from '@workspace/ui/components/Badge'
import { Button } from '@workspace/ui/components/Button'
import { dateFormat } from '@workspace/lib/utils/date-time'
import { SettingsIcon } from '@/shared/icon'

const statuses = [
    { id: 'ACTIVE', name: 'Active' },
    { id: 'INACTIVE', name: 'Inactive' },
]

interface Tenant {
    id: string
    name: string
    code: string
    contact: string
    country: string
    status: string
    created: string
}

const columnHelper = createColumnHelper<Tenant>()

const columns = [
    columnHelper.accessor('name', {
        header: 'Tenant Name',
        cell: info => <p className="font-medium">{info.getValue()}</p>,
        enableSorting: true,
    }),
    columnHelper.accessor('code', {
        header: 'Code',
        cell: info => <p className="font-medium">{info.getValue()}</p>,
        enableSorting: true,
    }),
    columnHelper.accessor('created', {
        header: 'Created',
        cell: info => dateFormat(info.getValue()),
        enableSorting: true,
    }),
    columnHelper.accessor('status', {
        header: 'Status',
        size: 100,
        cell: info => (
            <Badge variant={info.getValue() === 'Active' ? 'default' : 'destructive'}>{info.getValue()}</Badge>
        ),
        enableSorting: false,
    }),
    columnHelper.accessor('contact', {
        header: 'Contact',
        enableSorting: false,
    }),
    columnHelper.accessor('country', {
        header: 'Country',
        enableSorting: false,
    }),
    columnHelper.display({
        id: 'actions',
        header: 'Actions',
        cell: () => (
            <Button variant="ghost" size="icon" aria-label="edit">
                <SettingsIcon />
            </Button>
        ),
        enableSorting: false,
    }),
]
