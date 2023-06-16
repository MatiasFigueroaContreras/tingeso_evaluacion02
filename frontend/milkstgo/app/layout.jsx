import "@/styles/globals.css"
import Navbar from "@/components/Navbar"

export const metadata = {
    tittle: "MilkStgo"
}

export const RootLayout = ({children}) => {
  return (
   <html lang='es'>
    <body>
        <Navbar />
        <main className='app'>{children}</main>
    </body>
   </html>
  )
}

export default RootLayout;