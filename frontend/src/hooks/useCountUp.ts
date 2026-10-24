import { useEffect, useRef, useState } from 'react'

export function useCountUp(target: number, duration = 1200) {
    const ref = useRef<HTMLSpanElement>(null)
    const [value, setValue] = useState(0)

    useEffect(() => {
        const element = ref.current
        if (!element) return

        let triggered = false

        const observer = new IntersectionObserver(
            ([entry]) => {
                if (!entry.isIntersecting || triggered) return
                triggered = true

                const start = performance.now()

                const step = (now: number) => {
                    const elapsed = now - start
                    const progress = Math.min(elapsed / duration, 1)
                    const eased = 1 - (1 - progress) * (1 - progress) // ease-out
                    setValue(Math.round(eased * target))
                    if (progress < 1) requestAnimationFrame(step)
                }

                requestAnimationFrame(step)
            },
            { threshold: 0.4 },
        )

        observer.observe(element)
        return () => observer.disconnect()
    }, [target, duration])

    return { ref, value }
}