import SideNav from '@/app/ui/fruits/sidenav';
import Header from '@/app/ui/fruits/header';
import { Suspense } from 'react';
import { SessionProvider } from "next-auth/react"

export default function Layout({children} : { children: React.ReactNode }) {

    return (

        <>

        <SessionProvider>
            <Suspense>
                <Header />
            </Suspense>
            <div className="flex h-screen flex-col md:flex-row md:overflow-hidden">
                  <div className="w-full flex-none md:w-64">
                    <SideNav />
                  </div>
                  <div className="flex-grow p-6 md:overflow-y-auto md:p-12">{children}</div>
            </div>

        </SessionProvider>
       </>

    );
}