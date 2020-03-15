export default function logRoute({router, from, to, next}) {
    if (process.env.NODE_ENV !== 'production') {
        console.log(`Routing from '${from.name}' to '${to.name}'`)
    }
    return next();
}
