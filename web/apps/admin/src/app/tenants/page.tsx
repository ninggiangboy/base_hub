'use client'

import { BsSearchField } from '@workspace/ui/components/Searchfield'
import { BsSelect } from '@workspace/ui/components/Select'
import { createColumnHelper } from '@tanstack/react-table'
import { Badge } from '@workspace/ui/components/Badge'
import { DataTable } from '@workspace/ui/components/DataTable'
import { Button } from '@workspace/ui/components/Button'
import { Pagination, PaginationPageSizeSelector } from '@workspace/ui/components/Pagination'
import { dateFormat } from '@workspace/lib/utils/date-time'
import { useEffect, useState } from 'react'
import { AddIcon, CloseIcon, SettingsIcon } from '@/shared/icon'

const statuses = [
    { id: 'active', name: 'Active' },
    { id: 'inactive', name: 'Inactive' },
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

const data: Tenant[] = [
    {
        id: '1',
        name: 'Acme Corp',
        code: 'AC1001',
        contact: 'alice@acme.com',
        country: 'USA',
        status: 'Active',
        created: '2024-01-15T10:00:00Z',
    },
    {
        id: '2',
        name: 'Globex Inc',
        code: 'GB2002',
        contact: 'bob@globex.com',
        country: 'Germany',
        status: 'Inactive',
        created: '2024-03-22T14:30:00Z',
    },
    {
        id: '3',
        name: 'Initech',
        code: 'IT3003',
        contact: 'carol@initech.com',
        country: 'France',
        status: 'Active',
        created: '2024-05-10T09:15:00Z',
    },
    {
        id: '4',
        name: 'Umbrella Corp',
        code: 'UM4004',
        contact: 'dave@umbrella.com',
        country: 'Spain',
        status: 'Inactive',
        created: '2024-07-18T11:45:00Z',
    },
    {
        id: '5',
        name: 'Soylent Corp',
        code: 'SC5005',
        contact: 'eve@soylent.com',
        country: 'Italy',
        status: 'Active',
        created: '2024-09-05T16:20:00Z',
    },
]

export default function Page() {
    const [isLoading, setIsLoading] = useState(false)

    const handleLoadData = () => {
        setIsLoading(true)
        setTimeout(() => {
            setIsLoading(false)
        }, 1000)
    }

    useEffect(() => {
        handleLoadData()
    }, [])

    return (
        <div className="w-full space-y-3">
            <div className="flex gap-2">
                <BsSearchField placeholder="Search tenants..." />
                <BsSelect
                    options={statuses}
                    selectionMode="single"
                    placeholder="Status"
                    className="w-[155px] max-sm:hidden"
                />
                <Button className="max-sm:hidden" variant="outline">
                    <CloseIcon />
                    Clear
                </Button>
                <Button className="ml-auto max-sm:hidden">
                    <AddIcon />
                    Add Tenant
                </Button>
            </div>
            <DataTable
                enableSorting
                data={isLoading ? [] : data}
                columns={columns}
                isLoading={isLoading}
                columnPinning={{ left: ['name'], right: ['actions'] }}
            />
            <div className="flex gap-4 justify-between">
                <PaginationPageSizeSelector options={[10, 20, 50, 100]} defaultValue={10} />
                <Pagination pageCount={10} />
            </div>
        </div>
    )
}
