"use client"

import React from 'react';
import FruitAddForm from '@/app/ui/fruits/components/fruit-add-form'
import FruitList from '@/app/ui/fruits/components/fruit-list'
import { Suspense } from 'react';
import { FruitListCardsSkeleton } from '@/app/ui/fruits/skeletons/skeletons'

export default function Page() {

    return (
        <>
            <div className="w-full max-w-xs">
                <FruitAddForm />
            </div>

            <Suspense fallback={<FruitListCardsSkeleton />}>
                <FruitList />
            </Suspense>

        </>
    );

}



