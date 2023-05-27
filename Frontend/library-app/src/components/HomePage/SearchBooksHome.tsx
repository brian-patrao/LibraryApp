export const SearchBooksHome = () => {
    return (
        <div className="p-5 mb-4 bg-dark top-book">
            <div
                className="container-fluid py-5 text-white 
                d-flex justify-content-center align-items-center"
            >
                <div>
                    <h1 className="display-5 fw-bold">
                        Welcome to NEU Library
                    </h1>
                    <p className="col-md-8 fs-4 text-nowrap">
                        Find the book you want
                    </p>
                    <a
                        type="button"
                        className="btn main-color btn-lg text-white"
                        href="/search"
                    >
                        Search Books
                    </a>
                </div>
            </div>
        </div>
    );
};
