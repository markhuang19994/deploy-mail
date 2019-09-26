self.addEventListener('notificationclick', event => {
    const notification = event.notification;
    const action = event.action;
    const link = notification.data.link;
    if (action !== 'close' && link) {
        clients.openWindow(link);
    }
    notification.close();
    console.log('notificationclick action is', action);
});