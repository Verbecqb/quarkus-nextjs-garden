import React from 'react';
import Image from 'next/Image';

export function FruitCard( {fruit} ) {

return (
    <>
        <div className = "mb-4 px-2 w-full sm:w-1/2 md:w-1/3 lg:w-1/4 xl:w-1/6">
            <div key = {fruit.id} className = "max-w-sm rounded overflow-hidden shadow-lg">
                <Image className ="w-full" src = "/hero-desktop.png" width = {320} height= {180} alt = "Sunset in the mountains" />

                <div className = "px-6 py-4">
                    <div className= "font-bold text-xl mb-2"> {fruit.name}</div>
                    <p className = "text-gray-700 text-base"> {fruit.description}</p>
                </div>

                <div className = "px-6 pt-4 pb-2">
                    <button type = "button" className = "inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2 mb-2">
                    Add to shopping card
                    </button>
                    <span className = "inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2 mb-2">#photography</span>
                </div>
            </div>
        </div>

    </>
    )

}