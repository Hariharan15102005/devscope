export default function ErrorBox({ message, onRetry }) {
  return (
    <div className="ds-error-box" role="alert">
      <p>{message}</p>
      {onRetry && (
        <button className="ds-btn" type="button" onClick={onRetry}>
          Retry
        </button>
      )}
    </div>
  );
}
