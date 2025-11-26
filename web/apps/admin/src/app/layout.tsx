import { Providers } from '@/shared/components/Providers'
import { DashboardLayout } from '@/shared/layouts/DashboardLayout'
import '@workspace/ui/globals.css'
import { cookies } from 'next/headers'

interface LayoutProps {
    children: React.ReactNode
}

export default async function RootLayout({ children }: Readonly<LayoutProps>) {
    const cookieStore = await cookies()
    const defaultOpen = cookieStore.get('sidebar_state')?.value !== 'false'
    return (
        <html lang="en" translate="no" suppressHydrationWarning>
            <body className={`font-sans antialiased`}>
                <Providers>
                    <DashboardLayout defaultOpen={defaultOpen}>{children}</DashboardLayout>
                </Providers>
            </body>
        </html>
    )
}
