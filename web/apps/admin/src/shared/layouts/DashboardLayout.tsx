'use client'

import { Avatar, AvatarFallback } from '@workspace/ui/components/Avatar'
import {
    DashboardHeader,
    SidebarNavigationMenu,
    SidebarNavigationMenuItem,
} from '@workspace/ui/components/Sidebar.helpers'
import {
    Sidebar,
    SidebarContent,
    SidebarFooter,
    SidebarHeader,
    SidebarInset,
    SidebarProvider,
} from '@workspace/ui/components/Sidebar'
import Link from 'next/link'
import { usePathname } from 'next/navigation'
import { DashboardIcon, TenantIcon, UserIcon } from '../icon'

const MAIN_ITEMS: Array<SidebarNavigationMenuItem> = [
    {
        title: 'Dashboard',
        icon: DashboardIcon,
        url: '/dashboard',
    },
    {
        title: 'Tenants',
        icon: TenantIcon,
        url: '/tenants',
    },
    {
        title: 'Users',
        icon: UserIcon,
        url: '/users',
    },
]

interface DashboardLayoutProps {
    children: React.ReactNode
    defaultOpen?: boolean
}

export function DashboardLayout({ children, defaultOpen = true }: DashboardLayoutProps) {
    const currentPathname = usePathname()

    return (
        <SidebarProvider defaultOpen={defaultOpen}>
            <Sidebar collapsible="icon">
                <SidebarHeader>
                    <h1 className="text-2xl font-bold">BS</h1>
                </SidebarHeader>

                <SidebarContent>
                    <SidebarNavigationMenu linkComponent={Link} items={MAIN_ITEMS} currentPathname={currentPathname} />
                </SidebarContent>

                <SidebarFooter>{/* footer  */}</SidebarFooter>
            </Sidebar>

            <SidebarInset>
                <DashboardHeader>
                    <Avatar className="ml-auto">
                        <AvatarFallback>HP</AvatarFallback>
                    </Avatar>
                </DashboardHeader>
                <div className="p-6 h-full">{children}</div>
            </SidebarInset>
        </SidebarProvider>
    )
}
