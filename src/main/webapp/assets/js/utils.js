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

function postFormIfValid($form)
{
    return new Promise(((resolve, reject) =>
    {
        checkFormValidity($form[0]).then(valid =>
        {
            if(valid)postForm($form.attr('action'), $form).then(response => resolve(response)).catch(response => reject(response));
            else reject(new Error('invalid form'));
        });
    }));
}

function postForm(input, $form)
{
    /*return new Promise(((resolve, reject) =>
    {
        let request =  new XMLHttpRequest();
        request.onreadystatechange = function()
        {
            if (request.readyState === XMLHttpRequest.DONE) {
                if (request.status >= 400)
                {
                    console.log(request.responseText);
                    reject(request);
                }
                else resolve(request);
            }
        }

        request.open("POST", input, true);

        if($form.attr('enctype') === 'multipart/form-data')
        {
            let data = new FormData($form[0]);
            //request.setRequestHeader('Content-Type', 'multipart/form-data');
            request.send(data);
        }
        else
        {
            request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            request.send($form.serialize());
        }


    }));*/
    let enctype = !$form.attr('enctype') ? 'application/x-www-form-urlencoded' : $form.attr('enctype');

    return fetch(input, {
        method: 'post',
        headers:{
            'Content-Type': enctype
        },
        body: enctype === 'multipart/form-data' ? new FormData($form[0]) : $form.serialize()
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

    return new Promise((resolve, reject) =>
    {
        if(element.dataset.novalidate || element.disabled)
        {
            setValidity(element, true, true);
            resolve({local: true, online: true});
            return;
        }

        let readOnly = false;
        if(element.readOnly)
        {
            readOnly = true;
            element.readOnly = false;
        }
        let localValidity = checkLocalValidity(element);
        element.readOnly = readOnly;

        let onlineValidation = element.dataset.validation;
        if(localValidity && onlineValidation)
        {
            setValidity(element, true, false);
            let name = element.getAttribute('name');

            let exceptions = element.dataset.exceptions ? element.dataset.exceptions.split(',') : [];

            if(exceptions.includes(element.value))
            {
                setValidity(element, true, true);
                resolve({local: localValidity, online: true});
            }
            else
            {
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
        //.filter(element => !element.disabled && !element.dataset.novalidate)
        Array.from(form.querySelectorAll('input, select')).forEach(element => promises.push(checkElementValidity(element)));
        Promise.all(promises).then(values =>
            resolve(values.reduce((result, validity) =>
            {
                if(typeof validity.online !== "undefined") return result && validity.online;
                return result && validity.local;
            }, true)));
    });
}

export {serializeJson, post, postForm, postFormIfValid, showError, checkFormValidity, checkElementValidity}
