document.addEventListener('DOMContentLoaded', function() {
    const ws = new WebSocket('ws://localhost:8080/feeder');
    ws.onopen = function() {
        console.log('Connected to websocket');
        ws.send("view|" + JSON.stringify({
            url: window.location.href
        }))
    };
    ws.onmessage = function(event) {
        console.log(event.data);
    };
    ws.onclose = function() {
        console.log('Disconnected from websocket');
    };

    setInterval(function() {
        console.log('Sending heartbeat');
        ws.send('HEARTBEAT');
    }, 1000);

    // Send a "click" event anytime any element is clicked.
    document.addEventListener('click', function(event) {
        // Send the element's ID and the URL of the page it was clicked on.
        const url = window.location.href;
        const id = event.target.id ? event.target.id : event.target.tagName;
        console.log(id);

        console.log('Clicked element with ID ' + id + ' on page ' + url);
        ws.send("click|" + JSON.stringify({
            url: url,
            elementDescriptor: id
        }));
    });

    // The switch event is triggered when the user clicks on a link.
    window.addEventListener('popstate', (event) => {
        const currentUrl = window.location.href;
        ws.send("switch|" + JSON.stringify({
            destination: currentUrl,
        }))
    });

});