
/*=============== SCROLL REVEAL ANIMATION ===============*/
const sr = ScrollReveal({
    distance: '90px',
    duration: 3000,
})

sr.reveal(`.error__data`, {origin: 'top', delay: 400})
sr.reveal(`.error__img`, {origin: 'bottom', delay: 600})
sr.reveal(`.home__data`, {origin: 'top', delay: 400})
sr.reveal(`.home__img`, {origin: 'bottom', delay: 600})
sr.reveal(`.custom__footer`, {origin: 'bottom', delay: 400})