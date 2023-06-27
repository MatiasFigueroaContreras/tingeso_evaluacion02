import "@/styles/button-styled.css"

export default function ButtonStyled({text, disabled}) {
  return (
      <button className="button-style" type="submit" disabled={disabled}>
          <span className="button-text">{text}</span>
          {disabled && (
              <div className="loading-container">
                  <div className="loading-circle"></div>
              </div>
          )}
      </button>
  );
}
