function toggleVisibility(caller) {
    const maskField = document.getElementById('maskField');
    const maskFieldInput = document.getElementById('networkMask');
    const clientField = document.getElementById('clientField');
    const clientFieldInput = document.getElementById('clientsAmount');
    const paddingField = document.getElementById('paddingField');

    if (maskField.style.display === 'none') {
        if (caller === 'clientField')
            return;

        maskField.style.display = 'block';
        maskFieldInput.required = true;
        clientField.style.display = 'none';
        clientFieldInput.required = false;
        paddingField.style.display = 'none';
    } else {
        if (caller === 'maskField')
            return;

        maskField.style.display = 'none';
        maskFieldInput.required = false;
        clientField.style.display = 'block';
        clientFieldInput.required = true;
        paddingField.style.display = 'block';
    }
}