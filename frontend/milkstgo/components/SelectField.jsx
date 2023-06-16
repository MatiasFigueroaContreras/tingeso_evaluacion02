import "@/styles/select-field.css";

export default function SelectField({name, placeholder, options = [], onChange}) {
    return (
        <select name={name} onChange={onChange} className="select" defaultValue="" required>
            <option value="" disabled>
                {placeholder}
            </option>
            {options.map((option, index) => (
                <option key={index} value={option}>
                    {option}
                </option>
            ))}
        </select>
    );
}
