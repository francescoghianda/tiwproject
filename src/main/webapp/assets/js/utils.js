/**
 * @return {string}
 */
function serializeJson(json)
{
    return Object.keys(json).map(key => `${key}=${json[key]}`).join('&');
}

function showError($errorBox)
{
    $errorBox.addClass('show');
    setTimeout(function() {
        $errorBox.removeClass('show');
    }, 2500);
}

function postForm(input, $form)
{
    return fetch(input, {
        method: 'post',
        headers:{
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: $form.serialize()
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

function checkElementValidity(element)
{
    let localValidity = checkLocalValidity(element);
    let onlineValidation = element.dataset.validation;
    if(localValidity && onlineValidation)
    {
        setValidity(element, true, false);
        let name = element.getAttribute('name');
        let request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if(this.readyState === 4 && this.status === 200)
            {
                let json = JSON.parse(this.responseText);
                let onlineValidity = json[`valid-${name}`];
                if(onlineValidity)setValidity(element, true, true);
                else setValidity(element, false, true);
            }
        };
        request.open('GET', `${onlineValidation}?${name}=${element.value}`, true);
        request.send();
    }
    else
    {
        setValidity(element, true, true);
        if(localValidity) setValidity(element, true, false);
        else setValidity(element, false, false);
    }
}

function checkLocalValidity(element)
{
    if(element.tagName.toLowerCase() === 'select')
    {
        console.log(element + " "+element.options[element.selectedIndex].disabled);
        return element.checkValidity() && !element.options[element.selectedIndex].disabled
    }
    else return element.checkValidity();
}

function setValidity(element, valid, online)
{
    let formSection = $(element).closest('.form-section').get(0);
    let className = online ? 'online-invalid' : 'invalid';

    if(valid)
    {
        element.classList.remove(className);
        if(formSection)formSection.classList.remove(className);
    }
    else
    {
        element.classList.add(className);
        if(formSection)formSection.classList.add(className)
    }
}

function checkFormValidity(form)
{
    let validForm = true;
    form.querySelectorAll('input, select').forEach(element =>
    {
        let valid = checkLocalValidity(element);
        setValidity(element, valid, false);
        validForm = validForm && valid;
    });
    return validForm;
}

export {serializeJson, post, postForm, showError, checkFormValidity, checkElementValidity}