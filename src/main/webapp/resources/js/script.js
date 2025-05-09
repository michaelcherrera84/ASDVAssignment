let beanObject;
let isChanged = false;

/**
 * Set the value of isChanged.
 */
function change() {isChanged = true;}

/**
 * Set the value of isChanged.
 */
function changed(file, callback) {
    change();
    callback.call(this, file);
}

/**
 * If form fields have changed, confirm before closing the browser window.
 */
window.addEventListener('beforeunload', function (e) {
    if (isChanged) {
        e.preventDefault();
        isChanged = false;
    }
});

/**
 * Return the number of files submitted to the PrimeFaces FileUpload.
 *
 * @returns {number} the number files submitted
 */
function getFileCount(bean) {
    return PF(bean + 'file_upload').files.length;
}

/**
 * Send the number of files being uploaded to the bean.
 */
function uploadCount() {
    let bean = document.getElementsByName('bean');
    bean.forEach((element) => {
        if (getFileCount(element.value) > 0) {
            attemptedCount([{name: 'count', value: getFileCount(element.value)}]);
        }
    });

    setTimeout(function() {PF('status').hide()}, 1000);
}

/**
 * Send file count of files waiting to the server and upload the files to the server.
 */
function save() {
    let buttons = document.getElementsByClassName('ui-fileupload-upload');
    for (let i = 0; i < buttons.length; i++) {
        buttons[i].click();
    }
}

/**
 * Send file count of files waiting to the server.
 */
function checkFiles() {
    let bean = document.getElementsByName('bean');
    bean.forEach((element) => {
        if (getFileCount(element.value) > 0) {
            setFileWaiting([{name: 'filesWaiting', value: getFileCount(element.value)}]);
        }
    });
}

/**
 * Scroll to the bottom of the page.
 */
function scrollToBottom() {
    window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth'});
}