import React from "react";
import Image from 'next/Image';

import { FruitCard } from '@/app/ui/fruits/components/fruit-card';
import { fetchFruits } from '@/app/lib/data';

export default async function FruitList() {

     const data = await fetchFruits();

     return (
         <>
                  <div className="px-2">
                      <div className="flex flex-wrap -mx-2">

                        {data && data.map((f) => <FruitCard key={f.id} fruit={f} />)}
                      </div>
                 </div>

         </>
     )
}