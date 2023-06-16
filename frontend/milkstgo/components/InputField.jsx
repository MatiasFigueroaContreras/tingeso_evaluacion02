import "@/styles/input-field.css"

export default function InputField({ name, type, placeholder, onChange, ...extra }) {
    return (
        <input
            name={name}
            className="input"
            type={type}
            placeholder={placeholder}
            required
            onChange={onChange}
            {...extra}
        />
    );
}
