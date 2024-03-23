'use client'

import React from "react";

import { useState } from 'react';
import { useRouter } from 'next/navigation';

import { post } from '@/app/lib/data';

export default function FruitAddForm() {

    const router = useRouter();

    const [formData, setFormData] = React.useState({name: "", description: ""});

    const handleChange = (event) => {
         const {name, value} = event.target;
         setFormData((prevFormData)
             => ({ ...prevFormData, [name]: value }));
    }

    const handleSubmit = (event) => {

        event.preventDefault();

        const data = JSON.stringify({ "name": formData.name, "description": formData.description });
        post('http://localhost:8080/fruits', data)
            .then((response) => {

                if (response.ok) {

                    //empty the fields etc..
                    setFormData({name: "", description: ""});

                    // refresh the route (will refresh the other components)
                    router.replace("/ui/fruits", { scroll: false });
                    router.refresh();


                } else {
                    console.log('There has been a problem the response; ', response)
                }
        });


    };


    return (
        <form onSubmit={handleSubmit} className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">

                        <div className="mb-4">
                            <label htmlFor="nameFruit" className="block text-gray-700 text-sm font-bold mb-2">Name: </label>
                            <input id="nameFruit" type="input" name="name" value={formData.name} onChange={handleChange} className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
                        </div>

                        <div className="mb-6">
                            <div className="block text-gray-700 text-sm font-bold mb-2">
                                <label htmlFor="descriptionFruit">Description: </label>

                                <input id="descriptionFruit" type="input"
                                        name="description" value={formData.description} onChange={handleChange}
                                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
                            </div>
                        </div>
                        <div className="flex items-center justify-between">
                              <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="submit" value="Send">
                                Submit
                              </button>
                        </div>
        </form>

    )

}