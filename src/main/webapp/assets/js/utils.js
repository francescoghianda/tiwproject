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
    let validationBinding = element.dataset.bind_validation;
    if(validationBinding)checkElementValidity(document.getElementById(validationBinding));

    if(element.dataset.novalidate)return true;

    return new Promise((resolve, reject) =>
    {
        let localValidity = checkLocalValidity(element);
        let onlineValidation = element.dataset.validation;
        if(localValidity && onlineValidation)
        {
            setValidity(element, true, false);
            let name = element.getAttribute('name');

            fetch(`${onlineValidation}?${name}=${element.value}`).then(async response =>
            {
                if(response.status === 200)
                {
                    let json = await response.json();
                    let onlineValidity = json[`valid-${name}`];
                    setValidity(element, onlineValidity, true);
                    resolve({local: localValidity, online: onlineValidity});
                }
                else if(response.status !== 200) resolve({local: localValidity, online: undefined});
            });
        }
        else
        {
            setValidity(element, true, true);
            setValidity(element, localValidity, false);
            resolve({local: localValidity, online: undefined});
        }
    });
}

function checkLocalValidity(element)
{
    if(element.tagName.toLowerCase() === 'select') return element.checkValidity() && !element.options[element.selectedIndex].disabled;
    if(element.getAttribute('type') === 'hidden' && element.hasAttribute('required')) return !!element.value;
    let equalsTo = element.dataset.equals_to;
    if(equalsTo) return element.value === document.getElementById(equalsTo).value && element.checkValidity();
    else return element.checkValidity();
}

function setValidity(element, valid, online)
{
    let formSection = $(element).closest('.input-container').get(0);
    if(!formSection)return;

    let className = online ? 'online-error' : 'local-error';

    if(valid)
    {
        element.classList.remove('error')
        formSection.classList.remove('error');
        formSection.classList.remove(className);
    }
    else
    {
        element.classList.add('error')
        formSection.classList.add('error');
        formSection.classList.add(className);
    }
}

function checkFormValidity(form)
{
    return new Promise((resolve, reject) =>
    {
        let promises = [];
        Array.from(form.querySelectorAll('input, select')).filter(element => !element.disabled && !element.dataset.novalidate).forEach(element => promises.push(checkElementValidity(element)));
        Promise.all(promises).then(values =>
            resolve(values.reduce((result, validity) =>
            {
                if(typeof validity.online !== "undefined") return result && validity.online;
                return result && validity.local;
            }, true)));
    });
}

export {serializeJson, post, postForm, showError, checkFormValidity, checkElementValidity}
