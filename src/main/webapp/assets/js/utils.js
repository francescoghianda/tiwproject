/**
 * @return {string}
 */
function JSONToParams(json)
{
    return Object.keys(json).map(key => `${key}=${json[key]}`).join('&');
}

function postParams(input, jsonData)
{
    return fetch(input, {
        method: 'post',
        headers:{
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: JSONToParams(jsonData)
    })
}

function post(input, jsonData)
{
    return fetch(input, {
        method: 'post',
        headers:{
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonData)
    })
}

export {JSONToParams, post, postParams}