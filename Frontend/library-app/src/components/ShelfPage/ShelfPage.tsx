import { Loans } from "./Loans";

export const ShelfPage = () => {
    return (
        <div className="container">
            <div className="mt-3">
                <nav>
                    <div className="nav nav-tabs" id="nav-tab" role="tablist">
                        <button
                            className="nav-link active"
                            id="nav-loans-tab"
                            data-bs-toggle="tab"
                            data-bs-target="#nav-loans"
                            type="button"
                            role="tab"
                        >
                            Borrowed
                        </button>
                    </div>
                </nav>
                <div className="tab-content" id="nav-tabContent">
                    <div
                        className="tab-pane fade show active"
                        id="nav-loans"
                        role="tabpanel"
                        aria-labelledby="nav-loans-tab"
                    >
                        <Loans />
                    </div>
                </div>
            </div>
        </div>
    );
};
