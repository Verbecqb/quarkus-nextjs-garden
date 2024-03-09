

export async function post(url, data) {

    try {

        return await fetch(url, {
                                method: "POST",
                                mode: 'cors',
                                body: data,
                                headers: {
                                    'Content-Type': 'application/json; charset=utf-8',
                                     'Access-Control-Allow-Origin': '*',
                                }
                            });


    } catch(error) {
        console.log('There has been a problem with your post operation: ', error.message)
    }
}

export async function fetchFruits() {
    //  opt out of static rendering
    //no_store();

    try {

        const response = await fetch("http://localhost:8080/fruits", {
                                         method: "GET",
                                         mode: 'cors',
                                         headers: {
                                           "Content-type": "application/json; charset=UTF-8",
                                         }
                                     });

        const result = await response.json();
        return result;

    } catch(error) {
        console.error('GET API Error:', error);
    }
}

